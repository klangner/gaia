import { Component } from '@angular/core';
import { AmplifyService }  from 'aws-amplify-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Scenarios';
  authenticated = false;
  user = null;

  constructor( public amplify: AmplifyService ) {
    // handle auth state changes
    this.amplify.authStateChange$
      .subscribe(authState => {
        this.authenticated = authState.state === 'signedIn';
        if (!authState.user) {
          this.user = null;
        } else {
          this.user = authState.user;
        }
    });
  } 
}
