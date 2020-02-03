export class RestUserService {

    getAll() {
        return fetch('/api/v1/user/all');
    }

    getById(id) {
        return fetch('/api/v1/user/' + id);
    }

    deleteById(id) {
        return fetch('/api/v1/user/delete/' + id);
    }

    create(user) {
        return fetch('/api/v1/user/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user)
            }
        );
    }
}