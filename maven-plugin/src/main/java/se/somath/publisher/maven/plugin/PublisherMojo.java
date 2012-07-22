package se.somath.publisher.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import se.somath.publisher.Main;

/**
 * @goal publish
 * @phase generate-resources
 */
public class PublisherMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        Main main = new Main();
        main.copyToTarget();
    }
}