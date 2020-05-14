// recuperer la liste de proposition
$.ajax({

	success:function(resp){
		data : JSON.parse(resp)
		$.each(data, function(idx){
			atk = data[idx]
			// creer un boutton 
			// action du bouton -> id
			
			
		})
		
		// boutton abandonner
		
		
	}
})

// recupere la liste attaque du monstre
function quelleAttaqueOublier(e){
	
	$.ajax({
		success:function(resp){
			data : JSON.parse(resp)
			$.each(data, function(idx){
				atk = data[idx]
				// creer un boutton 
				// action du bouton -> id 
				
				
			})
			// retour
			// abandonner			
		}
	})
	
}

function learn(e){
	
	$.ajax({
		data : {idLearn : e.data.learn, idForget : e.data.forget}
		success:function(resp){
			data : JSON.parse(resp)
			$.each(data, function(idx){
				atk = data[idx]
				// creer un boutton 
				// action du bouton -> id 
				
				
			})

		}
	})	
	
}