import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        RepositoryService service = new RepositoryService();
        org.eclipse.egit.github.core.Repository repository = new Repository();

        repository.setName("Hello-World");
        repository.setDescription("This is your first repository");
        repository.setHomepage("https://github.com");
        repository.setPrivate(false);
        repository.setHasIssues(true);
        repository.setHasWiki(true);

        service.getClient().setCredentials("gen-test-java", "asdfghjkl456#");
        try {
            service.createRepository(repository);
        }catch (RequestException re){
//            throw new Exception("Repository name already exists on this account.");
        }

        try (Git git = Git.init().setDirectory(new File("../appleball")).call()) {
            System.out.println("Created repository: " + git.getRepository().getDirectory());
            File myFile = new File(git.getRepository().getDirectory().getParent(), "testfile");
//            if (!myFile.createNewFile()) {
//                throw new IOException("Could not create file " + myFile);
//            }
            git.add().addFilepattern("testfile").call();
            git.add().addFilepattern("../appleball/src").call();

            RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName("origin");
            remoteAddCommand.setUri(new URIish("https://github.com/gen-test-java/Hello-World.git"));
            // you can add more settings here if needed
            remoteAddCommand.call();

            git.commit().setMessage("Initial commit").call();
            System.out.println("Committed file " + myFile + " to repository at " + git.getRepository().getDirectory());



            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("gen-test-java", "asdfghjkl456#"));
            // you can add more settings here if needed
            pushCommand.call();
//            Git git = Git.init().setDirectory("../appleball").call();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
