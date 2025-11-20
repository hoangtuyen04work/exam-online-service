package edu.exam_online.exam_online_system.utils;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        try{
            Long userId = SecurityUtils.getUserId();
            return Optional.of(userId);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
}
