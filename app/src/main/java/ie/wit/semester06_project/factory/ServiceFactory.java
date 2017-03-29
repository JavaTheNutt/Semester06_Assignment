package ie.wit.semester06_project.factory;

import ie.wit.semester06_project.service.DashboardService;
import ie.wit.semester06_project.service.EntryService;
import ie.wit.semester06_project.service.Util;

/**
 * Created by joewe on 24/02/2017.
 */
public class ServiceFactory
{

    private Util util;
    private DashboardService dashboardService;
    private EntryService entryService;

    /**
     * Gets util.
     *
     * @return the util
     */
    public Util getUtil()
    {
        if (util == null) {
            util = new Util();
        }
        return util;
    }

    /**
     * Gets dashboard service.
     *
     * @return the dashboard service
     */
    public DashboardService getDashboardService()
    {
        if (dashboardService == null) {
            dashboardService = new DashboardService();
        }
        return dashboardService;
    }

    /**
     * Gets entry service.
     *
     * @return the entry service
     */
    public EntryService getEntryService()
    {
        if (entryService == null) {
            entryService = new EntryService();
        }
        return entryService;
    }

}
