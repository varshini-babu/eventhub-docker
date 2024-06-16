import { useState } from "react";
import { registerUser } from "../utils/ApiFunctions"; // Assuming this is correctly implemented
import { Link } from "react-router-dom";

const Registration = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [registration, setRegistration] = useState({
    user_first_name: "",
    user_last_name: "",
    user_email: "",
    user_password: "",
  });

  const handleInputChange = (e) => {
    setRegistration({ ...registration, [e.target.name]: e.target.value });
  };

  const validatePassword = (password) => {
    const errors = [];
    const minLength = 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (password.length < minLength) {
      errors.push(`Password must be at least ${minLength} characters long.`);
    }
    if (!hasUpperCase) {
      errors.push("Password must contain at least one uppercase letter.");
    }
    if (!hasLowerCase) {
      errors.push("Password must contain at least one lowercase letter.");
    }
    if (!hasNumber) {
      errors.push("Password must contain at least one number.");
    }
    if (!hasSpecialChar) {
      errors.push("Password must contain at least one special character.");
    }
    
    return errors.join(' ');
  };

  const handleRegistration = async (e) => {
    e.preventDefault();
    const passwordError = validatePassword(registration.user_password);
    if (passwordError) {
      setErrorMessage(passwordError);
      setSuccessMessage("");
      return;
    }

    try {
      const result = await registerUser(registration);
      setSuccessMessage(result);
      setErrorMessage("");
      setRegistration({
        user_first_name: "",
        user_last_name: "",
        user_email: "",
        user_password: "",
      });
    } catch (error) {
      setSuccessMessage("");
      setErrorMessage(`Registration error: ${error.message}`);
    }
    setTimeout(() => {
      setErrorMessage("");
      setSuccessMessage("");
    }, 5000);
  };

  return (
    <section className="container col-6 mt-5 mb-5">
      {errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
      {successMessage && <p className="alert alert-success">{successMessage}</p>}

      <h2>Register</h2>
      <form onSubmit={handleRegistration}>
        <div className="mb-3 row">
          <label htmlFor="user_first_name" className="col-sm-2 col-form-label">
            First Name
          </label>
          <div className="col-sm-10">
            <input
              id="user_first_name"
              name="user_first_name"
              type="text"
              className="form-control"
              value={registration.user_first_name}
              onChange={handleInputChange}
            />
          </div>
        </div>

        <div className="mb-3 row">
          <label htmlFor="user_last_name" className="col-sm-2 col-form-label">
            Last Name
          </label>
          <div className="col-sm-10">
            <input
              id="user_last_name"
              name="user_last_name"
              type="text"
              className="form-control"
              value={registration.user_last_name}
              onChange={handleInputChange}
            />
          </div>
        </div>

        <div className="mb-3 row">
          <label htmlFor="user_email" className="col-sm-2 col-form-label">
            Email
          </label>
          <div className="col-sm-10">
            <input
              id="user_email"
              name="user_email"
              type="email"
              className="form-control"
              value={registration.user_email}
              onChange={handleInputChange}
            />
          </div>
        </div>

        <div className="mb-3 row">
          <label htmlFor="user_password" className="col-sm-2 col-form-label">
            Password
          </label>
          <div className="col-sm-10">
            <input
              type="password"
              className="form-control"
              id="user_password"
              name="user_password"
              value={registration.user_password}
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className="mb-3">
          <button type="submit" className="btn btn-hotel" style={{ marginRight: "10px" }}>
            Register
          </button>
          <span style={{ marginLeft: "10px" }}>
            Already have an account? <Link to={"/login"}>Login</Link>
          </span>
        </div>
      </form>
    </section>
  );
}

export default Registration;
