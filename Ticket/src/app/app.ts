import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { SessionActivityService } from './service/session-activity-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  constructor(private sessionActivityService: SessionActivityService) {
    this.sessionActivityService.initialize();
  }
}
