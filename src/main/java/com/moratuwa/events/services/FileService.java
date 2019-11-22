package com.moratuwa.events.services;

import com.moratuwa.events.exception.MyFileNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

	private final Path fileLocation;

	private Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	public FileService(@Value("${file.upload-dir}") String storageLocation) {
		this.fileLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
	}

	public String uploadFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (file.isEmpty()) {
				return null;
			}
			if (fileName.contains("..")) {
				return null;
			}
			String newFileName = UUID.randomUUID().toString() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
			logger.info("Creating file " + newFileName);
			Path targetLocation = this.fileLocation.resolve(newFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return newFileName;

		} catch (IOException e) {
			logger.error("Fail to save file", e);
			return null;
		}
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

}