package com.autohubreactive.emailnotification.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String CONTENT_TYPE = "text/plain";
    public static final String SUBJECT = "Invoice Notice";
    public static final String ENDPOINT = "mail/send";
    public static final String MAIL_TEMPLATE_FOLDER = "mail-template/";
    public static final String DATA_RESIDENCY = "eu";
    public static final String FILE_NAME = "invoice-notice";
    public static final String MUSTACHE_FORMAT = ".mustache";

}
