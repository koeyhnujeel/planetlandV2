package com.planetlandV2.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public class CreateFile {
	public static final String PATH = System.getProperty("user.dir") + "/src/main/resources/images/";

	public static String getImgName(MultipartFile imgFile) throws IOException {
		String originName = imgFile.getOriginalFilename();

		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + originName;

		imgFile.transferTo(new File(PATH + imgName));
		return imgName;
	}
}
