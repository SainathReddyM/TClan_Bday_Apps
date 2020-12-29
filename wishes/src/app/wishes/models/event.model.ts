import {Employee} from './employee.model';

export class EventModel {
    id: number;
    eventCode: string;
    employee: Employee;
    eventType: string;
    eventDate: string;

    constructor(event) {
        this.id = event.id;
        this.eventCode = event.eventCode;
        this.employee = new Employee(event.employee);
        this.eventType = event.eventType;
        this.eventDate = event.eventDate;
    }
}
