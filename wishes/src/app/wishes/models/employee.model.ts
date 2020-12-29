export class Employee {
    id: number;
    userId: string;
    emailId: string;
    firstName: string;
    lastName: string;
    role: string;
    dob: string;
    doj: string;
    installationStatus: string;

    constructor(emp) {
        this.id = emp.id;
        this.userId = emp.userId;
        this.emailId = emp.emailId;
        this.firstName = emp.firstName;
        this.lastName = emp.lastName;
        this.role = emp.role;
        this.dob = emp.dob;
        this.doj = emp.doj;
        this.installationStatus = emp.installationStatus;
    }
}
