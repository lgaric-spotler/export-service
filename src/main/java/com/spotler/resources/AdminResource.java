package com.spotler.resources;

import com.google.inject.Inject;
import com.spotler.core.business.ExportManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/admin")
public class AdminResource {

    private final ExportManager exportManager;

    @Inject
    public AdminResource(ExportManager exportManager) {
        this.exportManager = exportManager;
    }

    @GET
    @Path("/status")
    public String health() {
        return "OK";
    }

    @GET
    @Path("/export-data-lake")
    public String startDataLakeExport() {
        if (exportManager.isDataLakeExportRunning()) {
            return "ALREADY_RUNNING";
        }

        exportManager.runDataLakeExport(true);
        return "OK";
    }
}
