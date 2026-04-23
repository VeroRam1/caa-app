import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardEditor } from './board-editor';

describe('BoardEditor', () => {
  let component: BoardEditor;
  let fixture: ComponentFixture<BoardEditor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoardEditor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoardEditor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
