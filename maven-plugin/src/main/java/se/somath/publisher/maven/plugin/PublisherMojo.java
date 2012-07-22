package se.somath.publisher.maven.plugin;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import se.somath.publisher.Main;

import java.util.List;
import java.util.Map;

/**
 * @goal publish
 * @phase generate-resources
 */
public class PublisherMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {

        Map pluginContext = getPluginContext();
        MavenProject mavenProject = (MavenProject) pluginContext.get("project");
        Build build = mavenProject.getBuild();
        String targetDirectory = build.getDirectory();
        List<Resource> resources = build.getResources();
        String sourceDirectory = resources.get(0).getDirectory();

        Main main = new Main();
        main.copyToTarget(sourceDirectory, targetDirectory);
    }
}