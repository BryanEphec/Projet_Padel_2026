package com.padel.padelmanagement.service;

import com.padel.padelmanagement.entity.Site;
import com.padel.padelmanagement.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SiteService {
    @Autowired
    private SiteRepository siteRepository;

    public List<Site> getAllSites(){
        return siteRepository.findAll();
    }
}