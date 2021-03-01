package com.github.cyberpunkperson.config.cache;

import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.util.ReflectionUtils.doWithFields;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public interface CacheKey extends Serializable {


    @SneakyThrows
    default String buildCacheKey() {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        doWithFields(this.getClass(), field -> {
            makeAccessible(field);

            if (!field.isAnnotationPresent(CacheKeyOperation.Exclude.class)) {
                Optional.ofNullable(field.get(this))
                        .ifPresent(value -> updateDigest(messageDigest, (Serializable) value));
            }
        });

        return Hashing.sha256()
                .hashBytes(messageDigest.digest())
                .toString();
    }

    default void updateDigest(MessageDigest messageDigest, Serializable inputObject) {

        if (inputObject instanceof Collection) {
            ((Collection<?>) inputObject).forEach(object -> updateDigest(messageDigest, inputObject));
        } else {
            messageDigest.update(SerializationUtils.serialize(inputObject));
        }
    }

}
