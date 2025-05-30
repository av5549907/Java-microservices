package com.service.course.config;

import java.io.File;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER="0";
    public static final String DEFAULT_PAGE_SIZE="10";
    public static final String SORT_BY="title";
    public static final String SORT_DIRECTION="ascending";

    public static final String COURSE_BANNER_UPLOAD_DIR="uploads"+ File.separator+"courses"+File.separator+"banner";
    public static final String COURSE_VIDEO_UPLOAD_DIR="uploads"+ File.separator+"courses"+File.separator+"video";

    public static final String ROLE_ADMIN="ROLE_ADMIN";
    public static final String ROLE_GUEST="ROLE_GUEST";

    public static final String ADMIN="ADMIN";

    public static final String GUEST="GUEST";
    public static final String DEFAULT_USER_PROFILE_PATH="uploads"+File.separator+"default.jpg";

    public  static final String CATEGORY_SERVICE_URL="http://localhost:9098/api/v1/categories/";
    public  static final String VIDEO_SERVICE_URL="http://localhost:9093/api/v1/videos/";
}

