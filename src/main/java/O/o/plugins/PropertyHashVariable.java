package O.o.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.joget.apps.app.model.DefaultHashVariablePlugin;
import org.joget.commons.util.LogUtil;
import org.joget.commons.util.SetupManager;

public class PropertyHashVariable extends DefaultHashVariablePlugin {
    Map<String, Properties> propertiesCache = new HashMap<String, Properties>();
    
    public String getName() {
        return "Property Hash Variable";
    }
    
    public String getVersion() {
        return "1.0.0";
    }

    public String getDescription() {
        return "To retrieve a property value from a property file locate in wflow folder";
    }

    public String getPrefix() {
        return "property";
    }

    public String processHashVariable(String variableKey) {
        String fileName = variableKey.substring(0, variableKey.indexOf("."));
        String key = variableKey.substring(variableKey.indexOf(".") + 1);
        return getProperty(fileName, key);
    }

    @Override
    public Collection<String> availableSyntax() {
        Collection<String> syntax = new ArrayList<String>();
        syntax.add("property.FILE_NAME.KEY");
        
        return syntax;
    }
    
    protected String getPath(String fileName) {
        String path = SetupManager.getBaseSharedDirectory();
        path += fileName + ".properties";
        return path;
    }
    
    protected Properties getProperties(String fileName) {
        Properties properties = propertiesCache.get(fileName);
        if (properties == null) {
            properties = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(getPath(fileName)));
                properties.load(fis);
                propertiesCache.put(fileName, properties);
            } catch (FileNotFoundException e) {
            } catch (Exception e) {
                LogUtil.error(PropertyHashVariable.class.getName(), e, "");
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) {
                    LogUtil.error(PropertyHashVariable.class.getName(), e, "");
                }
            }
        }
        return properties;
    }
    
    protected String getProperty(String fileName, String key) {
        if (fileName != null && !fileName.isEmpty() && key != null && !key.isEmpty()) {
            Properties properties = getProperties(fileName);
            if (properties != null) {
                return properties.getProperty(key);
            }
        }
        return null;
    }
    
    public String getLabel() {
        return getName();
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getPropertyOptions() {
        return "";
    }
}
