document.addEventListener("DOMContentLoaded", () => {
    // Get success and error messages from hidden input fields
    const successMessage = document.getElementById("success-message")?.value;
    const errorMessage = document.getElementById("error-message")?.value;
  
    // Display success message if it exists
    if (successMessage) {
      window.toast.success(successMessage);
    }
  
    // Display error message if it exists
    if (errorMessage) {
      window.toast.error(errorMessage);
    }
  });
  