api_link = "http://localhost:8080/api/books"



function showNotFound(query) {

	const search_div = document.getElementsByClassName("search-results")[0]
	search_div.innerHTML = "";
	
		const noResult_div = document.createElement("div");
		noResult_div.setAttribute("class","no-results");
		
		const error_logo_div = document.createElement("div");
		error_logo_div.setAttribute("id","error-logo");
		
		const img = document.createElement("img");
		img.setAttribute("src","./images/error.png")
		
		error_logo_div.appendChild(img);
		
		const message = document.createElement("h3");
		
		message.innerHTML = "No results found for search query <span>" + query + "</span>";
		
		noResult_div.appendChild(error_logo_div);
		noResult_div.appendChild(message);
		
		search_div.appendChild(noResult_div);

}

function showresults(data) {
	const search_results = document.querySelector(".search-results");
	search_results.innerHTML = "";
	
		const heading = document.createElement("h3");
		heading.setAttribute("id","result-heading");
		heading.innerHTML = "Search Results";
		
		search_results.appendChild(heading);
		
		
		
		const container = document.createElement("div");
		container.setAttribute("class","book-list");
	  
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
			
			
			image_container.appendChild(img);
			item.appendChild(image_container);
			
			let info = document.createElement("div");
			info.setAttribute("class", "info");
			let title = document.createElement("h3");
			title.textContent = book.title;
			let rating = document.createElement("h3");
			rating.textContent = book.price+"$";
			
			info.appendChild(title);
			info.appendChild(rating);
			item.appendChild(info);
			
			item.addEventListener('click', () => {
			const itemurl = './item.html?' + 'id=' + book.id;
			window.location.href = itemurl;
			});
			
			anchor.appendChild(item);
			container.appendChild(anchor);
			search_results.appendChild(container);
		  }
	
}


async function performSearch(query){
	
	try{
		  let response = await(axios.get(api_link + "?query="+query,{
			  withCredentials : true,
			  headers: {
				"Content-Type":"application/json;charset=UTF-8"
			  }
		  }))
		  if(response.status == 200) {
			  books = response.data._embedded.book_DTOes;
			  console.log(books);
			  showresults(books);
			  
		  }
	  } catch (error) {
		  console.log("an error occured")
		  if(error.response) {
			  console.log(error.response);
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }else if (error.response.status == 404) {
					  showNotFound(query);
					  
				  }
			  }
			  
		  }
	  }

}



const search_box = document.getElementById("search-box");

search_box.addEventListener('keydown',(event) =>{
	if(event.key == 'Enter'){
		performSearch(document.getElementById('search-box').value);
		
	}
})

const search_icon = document.getElementById("search--icon")

search_icon.addEventListener("click",(e) => {
	e.preventDefault();
	
	performSearch(document.getElementById('search-box').value);
})



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
  
  
  window.onload = () => {
	try{
		let params = new URLSearchParams(document.location.search);
		let query = params.get('query');
		if(query){
			document.getElementById('search-box').value = query;
			performSearch(query)
		}
	}catch{
		console.log("an error occured");
	}
	
}
  
   