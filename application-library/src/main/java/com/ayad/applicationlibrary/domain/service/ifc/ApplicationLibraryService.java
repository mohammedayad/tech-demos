package com.ayad.applicationlibrary.domain.service.ifc;

import com.ayad.applicationlibrary.domain.model.Application;

import java.util.List;

public interface ApplicationLibraryService {

    List<Application> getApplicationLibraryFor(String userId);
}
