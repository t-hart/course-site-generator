package djf.components;

/**
 * This interface provides the structure for data components in
 * our applications.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public interface AppDataComponent {

    /**
     * This function would be called when initializing data.
     */
    public void resetData();
    
    public String getCSSDirForExport();
    
    public String getSchoolBannerDirForExport();
    
    public String getLeftFooterDirForExport();
    
    public String getRightFooterDirForExport();
    
    public String getExportDir();
}
