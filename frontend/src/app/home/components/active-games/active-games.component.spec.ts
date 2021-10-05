import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveGamesComponent } from './active-games.component';

describe('ActiveGamesComponent', () => {
  let component: ActiveGamesComponent;
  let fixture: ComponentFixture<ActiveGamesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveGamesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
