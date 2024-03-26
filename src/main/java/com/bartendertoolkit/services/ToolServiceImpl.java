package com.bartendertoolkit.services;

import com.bartendertoolkit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService{
    private final UserRepository userRepository;

    @Override
    public void calculateTips(Long userId, List<String> names, List<Float> hours, int totalTips) {

    }
}
