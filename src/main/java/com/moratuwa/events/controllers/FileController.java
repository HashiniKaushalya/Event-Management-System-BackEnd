package com.moratuwa.events.controllers;

import com.moratuwa.events.services.FileService;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
public class FileController {

	@Autowired
	FileService fileService;

	private Logger logger = LoggerFactory.getLogger(FileController.class);

	@PostMapping("/file/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		logger.info("upload file");

		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.OK);

		}
		String fileName = fileService.uploadFile(uploadfile);

		if (fileName == null) {
			return new ResponseEntity<>("failed to upload file", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(fileName, HttpStatus.OK);
	}

	@GetMapping("/file/{fileName:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String fileName, HttpServletRequest request) {

		Resource resource = fileService.loadFileAsResource(fileName);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.error("Could not determine file type.", ex);
		}
		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
