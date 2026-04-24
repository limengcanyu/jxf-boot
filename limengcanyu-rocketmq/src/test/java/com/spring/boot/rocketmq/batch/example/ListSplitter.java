package com.spring.boot.rocketmq.batch.example;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Split into lists
 * The complexity only grow when you send large batch and you may not sure if it exceeds the size limit (1MiB).
 *
 * At this time, you’d better split the lists:
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 13:37
 */
public class ListSplitter implements Iterator<List<Message>> {
    private final List<Message> messages;
    private int currIndex;

    public ListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;

        for (; nextIndex < messages.size(); nextIndex++) {
            Message message = messages.get(nextIndex);

            int tmpSize = message.getTopic().length() + message.getBody().length;

            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }

            tmpSize = tmpSize + 20; //for log overhead
            int SIZE_LIMIT = 1000 * 1000;

            if (tmpSize > SIZE_LIMIT) {
                //it is unexpected that single message exceeds the SIZE_LIMIT
                //here just let it go, otherwise it will block the splitting process
                if (nextIndex - currIndex == 0) {
                    //if the next sublist has no element, add this one and then break, otherwise just break
                    nextIndex++;
                }
                break;
            }

            if (tmpSize + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tmpSize;
            }

        }

        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;

        return subList;
    }
}
