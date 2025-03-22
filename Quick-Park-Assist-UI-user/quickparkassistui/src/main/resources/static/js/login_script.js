// Get the file input and profile image elements
const fileInput = document.getElementById("file-input-js");
const profileImage = document.getElementById("profile-image-js");

// Event listener for when a file is selected
fileInput.onchange = async (e) => {
  const file = e.target.files[0]; // Get the first selected file

  if (file) {
    // Generate a temporary URL for previewing the selected image
    const imageUrl = URL.createObjectURL(file);
    profileImage.src = imageUrl; // Update the profile image with the preview

    // Create FormData object to send file via POST request
    const formData = new FormData();
    formData.append("file", file);

    try {
      // Send the file to the server for upload
      const response = await fetch("/user/avatar", {
        method: "POST",
        body: formData,
      });

      // Parse the JSON response from the server
      const data = await response.json();

      // Show success or error message based on the response
      if (data.success) {
        toast.success(data.message);
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      // Handle errors during the upload process
      toast.error("Error uploading image: " + error.message);
    }
  }
};
