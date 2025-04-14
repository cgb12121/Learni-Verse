package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Video;
import com.hanu.leaniverse.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public void addNewVideo(Video video) {
        videoRepository.save(video);
    }

    public void deleteVideoById(int videoId) {
        videoRepository.deleteById(videoId);
    }
}
