import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { MembreService } from './membre.service';

describe('MembreService', () => {
  let service: MembreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        MembreService,
        provideHttpClient()
      ]
    });
    service = TestBed.inject(MembreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
