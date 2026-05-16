package org.asura.ddd.structure.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.asura.ddd.structure.user.domain.model.aggregate.User;
import org.asura.ddd.structure.user.domain.model.valueobject.Address;
import org.asura.ddd.structure.user.domain.model.valueobject.PhoneNumber;
import org.asura.ddd.structure.user.domain.repository.UserRepository;
import org.asura.ddd.structure.user.infrastructure.persistence.entity.UserEntity;
import org.asura.ddd.structure.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        if (userMapper.selectById(user.getId()) != null) {
            userMapper.updateById(entity);
        } else {
            userMapper.insert(entity);
        }
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        UserEntity entity = userMapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        UserEntity entity = userMapper.selectByUsername(username);
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        UserEntity entity = userMapper.selectByEmail(email);
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public void deleteById(String id) {
        userMapper.deleteById(id);
    }

    public IPage<User> findPage(int pageNum, int pageSize, String username, String email, Boolean enabled) {
        Page<UserEntity> page = new Page<>(pageNum, pageSize);
        IPage<UserEntity> entityPage = userMapper.selectPageByCondition(page, username, email, enabled);
        return entityPage.convert(this::toDomain);
    }

    public List<User> findList(String username, String email, Boolean enabled) {
        return userMapper.selectPageByCondition(new Page<>(), username, email, enabled)
                .getRecords()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        
        if (user.getPhoneNumber() != null) {
            entity.setPhoneCountryCode(user.getPhoneNumber().getCountryCode());
            entity.setPhoneNumber(user.getPhoneNumber().getNumber());
        }
        
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            entity.setAddressProvince(address.getProvince());
            entity.setAddressCity(address.getCity());
            entity.setAddressDistrict(address.getDistrict());
            entity.setAddressDetail(address.getDetail());
            entity.setAddressZipCode(address.getZipCode());
        }
        
        entity.setEnabled(user.getEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        PhoneNumber phoneNumber = null;
        if (entity.getPhoneNumber() != null) {
            phoneNumber = PhoneNumber.of(entity.getPhoneCountryCode(), entity.getPhoneNumber());
        }
        
        Address address = null;
        if (entity.getAddressProvince() != null) {
            address = Address.create(
                    entity.getAddressProvince(),
                    entity.getAddressCity(),
                    entity.getAddressDistrict(),
                    entity.getAddressDetail(),
                    entity.getAddressZipCode()
            );
        }
        
        return User.reconstruct(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                phoneNumber,
                address,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getEnabled()
        );
    }
}