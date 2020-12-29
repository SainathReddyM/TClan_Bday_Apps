import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { EnterWishComponent } from './enter-wish.component';

describe('EnterWishComponent', () => {
    let component: EnterWishComponent;
    let fixture: ComponentFixture<EnterWishComponent>;

    beforeEach(
        waitForAsync(() => {
            TestBed.configureTestingModule({
                declarations: [EnterWishComponent],
            }).compileComponents();
        })
    );

    beforeEach(() => {
        fixture = TestBed.createComponent(EnterWishComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
