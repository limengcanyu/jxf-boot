/**
 * <p>
 * Filter Example
 *
 * Principle
 * SQL feature could do some calculation through the properties you put in when sending messages. Under the grammars defined by RocketMQ, you can implement some interesting logic. Here is an example:
 *
 * ------------
 * | message  |
 * |----------|  a > 5 AND b = 'abc'
 * | a = 10   |  --------------------> Gotten
 * | b = 'abc'|
 * | c = true |
 * ------------
 * ------------
 * | message  |
 * |----------|   a > 5 AND b = 'abc'
 * | a = 1    |  --------------------> Missed
 * | b = 'abc'|
 * | c = true |
 * ------------
 *
 * Grammars
 *
 * RocketMQ only defines some basic grammars to support this feature. You could also extend it easily.
 *
 * 1. Numeric comparison, like >, >=, <, <=, BETWEEN, =;
 * 2. Character comparison, like =, <>, IN;
 * 3. IS NULL or IS NOT NULL;
 * 4. Logical AND, OR, NOT;
 *
 * Constant types are:
 *
 * 1. Numeric, like 123, 3.1415;
 * 2. Character, like ‘abc’, must be made with single quotes;
 * 3. NULL, special constant;
 * 4. Boolean, TRUE or FALSE;
 *
 * Usage constraints
 *
 * Only push consumer could select messages by SQL92. The interface is:
 *
 * public void subscribe(final String topic, final MessageSelector messageSelector)
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/21 13:45
 */
package com.spring.boot.rocketmq.filter.example;