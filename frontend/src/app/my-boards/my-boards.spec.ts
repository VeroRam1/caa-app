import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBoards } from './my-boards';

describe('MyBoards', () => {
  let component: MyBoards;
  let fixture: ComponentFixture<MyBoards>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBoards]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBoards);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
