package com.example.demo.generator.storage;

import com.example.demo.generator.model.GitHub;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubManager {

    private void createRepository(GitHub gitHub) throws Exception {
        RepositoryService service = new RepositoryService();
        Repository repository = new Repository();

        repository.setName("Hello-World");
        repository.setDescription("This is your first repository");
        repository.setHomepage("https://github.com");
        repository.setPrivate(false);
        repository.setHasIssues(true);
        repository.setHasWiki(true);

        service.getClient().setCredentials(gitHub.getUsername(), gitHub.getPassword());
        try {
            service.createRepository(repository);
        }catch (RequestException re){
            throw new Exception("Repository name already exists on this account.");
        }
    }

    public void gitPush(){

        PushPayload pushPayload = new PushPayload();
//        pushPayload.
    }
}
