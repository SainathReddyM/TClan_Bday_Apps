import { environment } from '../../../environments/environment';

const baseURL: string = environment.baseURL;

export const apiPaths = {
    testimonial: `${baseURL}/special-events/events`,
    event: `${baseURL}/special-events`,
    admin: `${baseURL}/admin/employees`,
    role: `${baseURL}/flock/employee-role`
};
