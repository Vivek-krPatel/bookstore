
 api_link = "http://localhost:8080/api"
  
  
  function itemGrid(data,container_div) {
  container_div.innerHTML = "";
  
	  for (book of data) {
		let anchor  = document.createElement("a");
		
		anchor.setAttribute("href","./item.html?id="+book.id);
		
		
		let item = document.createElement("div");
		item.setAttribute("class", "item");
		
		let image_container = document.createElement("div");
		image_container.setAttribute("class", "image-container");
		let img = document.createElement("img");
		img.src = book.image_url;
		
		image_container.appendChild(img);
		item.appendChild(image_container);
		
		let info = document.createElement("div");
		info.setAttribute("class", "info");
		let title = document.createElement("h3");
		title.textContent = book.title;
		let rating = document.createElement("h3");
		rating.textContent = book.rating;
		
		info.appendChild(title);
		info.appendChild(rating);
		item.appendChild(info);
		
		item.addEventListener('click', () => {
		const itemurl = './item.html?' + 'id=' + book.id;
		window.location.href = itemurl;
		});
		
		anchor.appendChild(item);
		container_div.appendChild(anchor);
  }
}
  
  async function fetchData(url) {
	  let books = [];
	  try{
		  let response = await(axios.get(url,{
			  withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}))
		  if(response.status == 200) {
			  books = response.data._embedded.book_DTOes;
			  return books;
			  
			  
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
  }
  
  
 async function bestsellers() {
	 const data = await fetchData(api_link + "/books");
	 const container = document.querySelector("#bestsellers");
	 itemGrid(data,container);
 }
 
  async function recommended() {
	 const data = await fetchData(api_link + "/books");
	 const container = document.querySelector("#recommended");
	 itemGrid(data,container);
 }
 
 async function isuserloggedin(){
	 try{
		  let response = await(axios.get(api_link+"/users/profile",{
			  withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}))
		  if(response.status == 200) {
			  document.getElementsByClassName("user-links")[0].style.display = "none";
			  document.getElementById("menu").style.display = "block";
			  
			  
		  }
	  } catch (error) {
		  if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  console.log("not logged in");

				  }
			  }
			  
		  }
	  }
 }
 
  const menu_button = document.getElementById("menu");
  
  const menu = document.getElementById("user");
  
  const close_button = document.getElementById("close");
  
  menu_button.addEventListener('click',(e) =>{
	  e.preventDefault();
	  
	  menu.style.display = "flex";
  
  })
  
  close_button.addEventListener("click",(e) =>{
	  e.preventDefault();
	  menu.style.display = "none";
  })
  
  
  
  const logout_button = document.getElementById("logout");
  logout_button.addEventListener("click", async (e) =>{
	  const logout_url = api_link +"/users/logout";
	  try{
					  let response = await(axios.get(logout_url,{
							withCredentials: true
						}));
					  
					  if(response.status == 204) {
						  console.log('logout success');
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
				
			 
		  
					  else if (error.request) {
						  console.log("No response received from the server");
						} else {
						  console.log("Error", error.message);
						}
		}
	  
  })
  
  
  
  window.onload = async () => {
	  isuserloggedin();
	  bestsellers();
	  recommended();
  };