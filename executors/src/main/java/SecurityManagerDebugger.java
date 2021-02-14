import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class SecurityManagerDebugger extends SecurityManager {
    private final List<String> denied = new ArrayList<>();

    @Override
    public void checkPermission(Permission permission) {
        try {
            super.checkPermission(permission);
        } catch (SecurityException se) {
            if (!denied.contains(permission.toString())) {
                System.out.println("SECURITY EXCEPTION: " + permission);
                denied.add(permission.toString());
            }
        } catch (NullPointerException ne) {
            System.out.println("NULL POINTER EXCEPTION: " + permission);
        }
    }

    @Override
    public void checkPermission(Permission permission, Object context) {
        try {
            super.checkPermission(permission, context);
        } catch (SecurityException se) {
            if (!denied.contains(permission.toString())) {
                System.out.println("SECURITY EXCEPTION: " + permission);
                denied.add(permission.toString());
            }
        } catch (NullPointerException ne) {
            System.out.println("NULL POINTER EXCEPTION: " + permission);
        }
    }
}
