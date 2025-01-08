package com.ayad.integrationservice.common.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcessorUtility {

    public static final String SERVER_ERROR_MESSAGE = "Server error";
    public static final String CONSTRAINT_VIOLATIONS = "CONSTRAINT_VIOLATIONS";
    public static final String FILE_STORAGE_ERROR = "FILE_STORAGE_ERROR";
    public static final String NO_Process_MATCHING_FOUND = "No Process has been found for ID: %s";

    public static final String STORE_ERROR_MESSAGE = "Failed to store file for Process ID: %s. Error: %s";
    public static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server error";
    public static final String ILLEGAL_ARGUMENT_ERROR_TITLE = "Illegal Argument Error";
    public static final String PROCESS_ID_NOT_FOUND_ERROR_TITLE = "Process ID Not Found Error";

}
