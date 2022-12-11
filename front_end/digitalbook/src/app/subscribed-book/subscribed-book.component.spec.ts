import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscribedBookComponent } from './subscribed-book.component';

describe('SubscribedBookComponent', () => {
  let component: SubscribedBookComponent;
  let fixture: ComponentFixture<SubscribedBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubscribedBookComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubscribedBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
