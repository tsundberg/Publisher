package se.somath.maven.plugins.publisher;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal publish
 */
public class PublisherMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        getLog().info("Doing nothing here");
    }
}