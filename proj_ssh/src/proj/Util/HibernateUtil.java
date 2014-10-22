package proj.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{
	private static final String FILE_LOC = "/hibernate.cfg.xml";
	
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    @SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() 
    {
        try 
        {
          // Create the SessionFactoryfrom hibernate.cfg.xml

          return new Configuration().configure(FILE_LOC)
        		                    .buildSessionFactory(); 
        }
        catch (Throwable ex) 
        {
          // Make sure you log the exception, as it might be
          System.err.println("Initial SessionFactorycreation failed." + ex);
          throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static Session getSession() 
    {
      return sessionFactory.getCurrentSession();
    }
}
