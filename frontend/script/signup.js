
api_link = "http://localhost:8080/api/users/register"

const btn = document.getElementById("signUp");



btn.addEventListener("click", async (event) => {
  event.preventDefault();

  const firstName = document.getElementById("firstName").value;
  const lastName= document.getElementById("lastName").value;
  const username = document.getElementById("userName").value;
  const email = document.getElementById("email").value;
  const phone = document.getElementById("phone").value;
  const password = document.getElementById("password").value;
  

  const payload = {
	  "firstName" : firstName,
	  "lastName" : lastName,
	 "username" : username,
	 "email" : email,
	 "phone" : phone,
	 "password" : password
  }
 console.log(payload);

	try{
			  let response = await(axios.post(api_link,payload,{
					withCredentials: false,
					headers : {
					"Content-Type":"application/json;charset=UTF-8"
				}}
			  ))
			  
			  if(response.status == 200) {
				  console.log('sign up successful');
				  window.location.href = "./index.html";
				  
			  }
		  } catch (error) {
			  if(error.response) {
				  if(error.response.status){
					  console.log(error.response.status);
					  if(error.response.status === 401) {
						  window.location.href = "./signin.html";
					  }
				  }
				  
			  }
		}
})






