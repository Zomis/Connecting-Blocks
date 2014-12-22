package net.zomis.connblocks.core;

import java.lang.Thread.UncaughtExceptionHandler;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Thanks to BinaryTweedDeej, http://badlogicgames.com/forum/viewtopic.php?f=11&t=10115
 */
public class LibGdxTestRunner extends BlockJUnit4ClassRunner
{
    public LibGdxTestRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
    }

    volatile boolean finished = false;


    @Override
    public void run(final RunNotifier notifier)
    {
        System.out.println("---------------------------------------------------");
        System.out.println(this);
        System.out.println("Starting run " + getDescription());
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 480;
        config.title = "Test";
        config.useGL30 = true;
        config.forceExit = false;

        final LibGdxTestRunner runner = this;
        LwjglApplication app = null;

        try
        {
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
            {

                @Override
                public void uncaughtException(Thread t, Throwable e)
                {
                    runner.finished = true;
                    EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
                    testNotifier.addFailure(e);
                }
            });

         /*
          * Gdx.gl was not getting nulled out between tests. It may not be
          * the direct cause, but there was no OpenGL context available after
          * a few runs; null out Gdx.gl and it works again.
          *
          * The 'correct' solution here is to use a new classloader for every
          * test class, but this works for now.
          */

            Gdx.gl = null;
            System.out.println("Gdx.gl: " + Gdx.gl);
            System.out.println("Thread in TestRunner: " + Thread.currentThread());
            app = new LwjglApplication(new ApplicationListener()
            {

                @Override
                public void resume()
                {
                }


                @Override
                public void resize(int width, int height)
                {
                }


                @Override
                public void render()
                {
                    if (!finished)
                    {
                        System.out.println("Running " + runner.getDescription().getDisplayName());

                        try
                        {
                            System.out.println("Thread in Render: " + Thread.currentThread());
                            runner.actualRun(notifier);
                        }
                        catch (Throwable t)
                        {
                            System.out.println("Throwable: " + t.getMessage());
                        }
                        finally
                        {
                            System.out.println("Finally " + runner.getDescription().getDisplayName());
                            runner.finished = true;
                        }
                    }
                }


                @Override
                public void pause()
                {
                }


                @Override
                public void dispose()
                {
                    System.out.println("Disposing");
                }


                @Override
                public void create()
                {
                }
            }, config);

            while (!finished)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    System.out.println("Interrupted");
                    e.printStackTrace();
                }
            }
        }
        catch (Throwable t)
        {
            System.out.println("b0rk");
            System.out.println(t.getMessage());
        }
        finally
        {
            if (app != null)
            {
                app.stop();
            }
        }

        System.out.println("Ending run " + getDescription());
    }


    public void actualRun(RunNotifier notifier)
    {
        super.run(notifier);
    }

}