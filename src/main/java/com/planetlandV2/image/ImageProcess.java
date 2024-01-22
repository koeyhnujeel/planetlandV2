package com.planetlandV2.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.planetlandV2.exception.planet.NotSupportedExtension;

@Component
public class ImageProcess {
	public static final String PATH = System.getProperty("user.dir") + "/src/main/resources/images/";

	private final List<String> extensionList = new ArrayList<>(List.of("jpg", "jpeg", "png", "bmp"));

	public void CheckExtension(MultipartFile imgFile) {
		String extension = StringUtils.getFilenameExtension(imgFile.getOriginalFilename());
		if (!extensionList.contains(extension)) {
			throw new NotSupportedExtension();
		}
	}

	public String getImageNameAndSave(MultipartFile imgFile) throws IOException {
		String originName = imgFile.getOriginalFilename();
		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + originName;

		imgFile.transferTo(new File(PATH + imgName));
		return imgName;
	}
}
