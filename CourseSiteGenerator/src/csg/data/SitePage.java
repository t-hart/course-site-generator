package csg.data;

/**
 *
 * @author tjhha
 */
public class SitePage {
    boolean use;
    String navbarTitle;
    String fileName;
    String script;
    
    public SitePage(boolean use, String navbarTitle, String fileName, String script){
        this.use = use;
        this.navbarTitle = navbarTitle;
        this.fileName = fileName;
        this.script = script;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public String getNavbarTitle() {
        return navbarTitle;
    }

    public void setNavbarTitle(String navbarTitle) {
        this.navbarTitle = navbarTitle;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
    
    
}
