import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatedBookComponent } from './created-book.component';

describe('CreatedBookComponent', () => {
  let component: CreatedBookComponent;
  let fixture: ComponentFixture<CreatedBookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatedBookComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatedBookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
