package co.edu.escuelaing.arsw;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class BBApplicationContextAware implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * Método para establecer el contexto de aplicación de Spring.
     * @param applicationContext Contexto de aplicación de Spring a establecer.
     * @throws BeansException Excepción lanzada si hay un error al acceder al contexto de aplicación.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * Método estático para obtener el contexto de aplicación de Spring.
     * @return Contexto de aplicación de Spring.
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
