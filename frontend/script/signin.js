
api_link = "http://localhost:8080/api/users/login"

const btn = document.getElementById("button");



btn.addEventListener("click", async (event) => {
  event.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  

  const payload = {
	  "username" : username,
	  "password" : password
  }

	try{
			  let response = await(axios.post(api_link,payload,{
					withCredentials: true,
					headers : {
					"Content-Type":"application/json;charset=UTF-8"
				}}
			  ));
			  
			  if(response.status == 200) {
				  console.log('log in succesful');
				  console.log(response.headers);
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






