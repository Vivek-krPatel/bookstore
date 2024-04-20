
  api_link = "http://localhost:8080/api"
  
  
  function itemGrid(data,container_div) {
  //let container = document.querySelector(".book-list");
  container_div.innerHTML = "";
  
	  for (book of data) {
		//console.log(book);
		let anchor  = document.createElement("a");
		
		anchor.setAttribute("href","./item.html?id="+book.id);
		
		
		let item = document.createElement("div");
		item.setAttribute("class", "book");
		
		let image_container = document.createElement("div");
		image_container.setAttribute("class", "book-image");
		let img = document.createElement("img");
		img.src = book.image_url;
		
		/*
		image_container.addEventListener('click', () => {
		  let itemurl = './item.html?' + 'id=' + book.id;
		  window.location.href = itemurl;
		});
		
		*/
		
		image_container.appendChild(img);
		item.appendChild(image_container);
		
		let info = document.createElement("div");
		info.setAttribute("class", "info");
		let title = document.createElement("h3");
		title.textContent = book.title;
		let rating = document.createElement("h3");
		rating.textContent = book.price;
		
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
	  let books;
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
  
 async function filterData(button) {
	 let currentUrl = window.location.href.split("?")[0];
	 let newUrl;

	  if (currentUrl.indexOf('?category') !== -1) {
		newUrl = currentUrl + ',' + button.innerHTML;
	  } else {
		newUrl = currentUrl + '?category=' + button.innerHTML;
	  }

	 window.history.pushState({path: newUrl}, '', newUrl);
	 const url = api_link + "/books/category?query=" + button.innerHTML;
	 let data = await fetchData(url);
	 const container = document.querySelector(".book-list");
	 itemGrid(data,container);
	 
	  
  }
  
  const search_button = document.getElementById("search--icon");
  search_button.addEventListener("click", (e) =>{
	  e.preventDefault();
	  const search_value = document.getElementById("search-input").value;
	  
	  window.location.href = "./search.html?query="+search_value;
	  
	  
  })
  
const search_input = document.getElementById("search-input");

search_input.addEventListener('keydown',(event) =>{
	if(event.key == 'Enter'){
	  const search_value = document.getElementById("search-input").value;
	  
	  window.location.href = "./search.html?query="+search_value;
	  
		
	}
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
	  const currentUrl = window.location.href;
	  if(currentUrl.indexOf('?') !== -1){
		
		const newUrl = api_link + "/books/filter" + window.location.search;
		const data = await fetchData(newUrl);
		const container = document.querySelector(".book-list");
		itemGrid(data,container);
	  }else{
		  const data = await fetchData(api_link+"/books");
		  const container = document.querySelector(".book-list");
		  itemGrid(data,container);
	  }
	  
  };