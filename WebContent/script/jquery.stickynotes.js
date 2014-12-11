(function(jQuery) {
	
	jQuery.fn.stickyNotes = function(options) {
		jQuery.fn.stickyNotes.options = jQuery.extend({}, jQuery.fn.stickyNotes.defaults, options);
		jQuery.fn.stickyNotes.prepareContainer(this);
		jQuery.each(jQuery.fn.stickyNotes.options.notes, function(index, note){
			if(index == jQuery.fn.stickyNotes.options.notes.length-1)
				jQuery.fn.stickyNotes.renderNote(note);
			jQuery.fn.stickyNotes.createScrollBarNote(note);
			jQuery.fn.stickyNotes.notes.push(note);
		});
	};
	
	//Get note from notes array
	jQuery.fn.stickyNotes.getNote = function(note_id) {
		var result = null;
		jQuery.each(jQuery.fn.stickyNotes.notes, function(index, note) {
			if (note.id == note_id) {
				result = note;
				return false;
			}
		});
		return result;
	}
	
	//Get all notes from notes array
	jQuery.fn.stickyNotes.getNotes = function() {
		return jQuery.fn.stickyNotes.notes;
	}	
	
	//Delete note from notes array
	jQuery.fn.stickyNotes.deleteNoteFromArray =  function(note_id) {
		jQuery.each(jQuery.fn.stickyNotes.notes, function(index, note) {
			if (note.id == note_id) {
				jQuery.fn.stickyNotes.notes.splice(index, 1);
				return false;
			}
		});
	}
	
	//Prepare main note container
	jQuery.fn.stickyNotes.prepareContainer = function(container) {
		jQuery(container).append('<div id="sticky-container" class="sticky-container"></div>');
	
		if (jQuery.fn.stickyNotes.options.controls) {
			jQuery("#add_note").click(function() {
				jQuery.fn.stickyNotes.createNote();
				return false;
			});
			
			jQuery("#delete_note1").click(function() {
				jQuery.fn.stickyNotes.deleteNote();
				return false;
			});		
		}
		
		jQuery("#sticky-container").click(function() {
			var note_ids = jQuery.fn.stickyNotes.currentlyEditedNotes();
			for (var i = note_ids.length - 1; i >= 0; i--){
				var note_id = note_ids[i]
				if (note_id != null) {
					jQuery.fn.stickyNotes.stopEditing(note_id);
				}				
			};
		}); 
	};
	
	//Create new note, called on click of new button
	jQuery.fn.stickyNotes.createNote = function() {
		//alert("note_content");
		var pos_x = 0;
		var pos_y = 0;		
		var note_id = jQuery.fn.stickyNotes.notes.length + 1;
		var _note_content = $(document.createElement('textarea')).attr("id", "textarea-note");
		//_note_content.attr('id', 'newnote');
		

		var _div_note 	= 	jQuery(document.createElement('div')).addClass('jStickyNote');
		
        var _div_background = jQuery.fn.stickyNotes.createNoteBackground();
		_div_note.append(_note_content);
		var _div_wrap 	= 	$(document.createElement('div'))
							.css({'position':'absolute','top':pos_x,'left':pos_y, 'float' : 'left'})
							.attr("id", "note-" + note_id)
							.append(_div_background)
							.append(_div_note);
		_div_wrap.addClass('jSticky-medium');		
		if (jQuery.fn.stickyNotes.options.resizable) {
			_div_wrap.resizable({stop: function(event, ui) { jQuery.fn.stickyNotes.resizedNote(note_id)}});
		}		
		
		var stickyContainer = $('#sticky-container');
		if(!stickyContainer.children().length){
			$('#sticky-container').append(_div_wrap);
		}else{
			var curr_note_id = stickyContainer.children().attr("id");
			var temp = jQuery("#" + curr_note_id).replaceWith(_div_wrap);
		}	
		
		jQuery.fn.stickyNotes.setCurrentlyEditedNote(note_id);
		
		jQuery("#note-" + note_id).click(function() {
			return false;
		});
		
		jQuery("#note-" + note_id).find("textarea").focus();
		var note = {"id":note_id,
		      "text":"",
			  "pos_x": pos_x,
			  "pos_y": pos_y,	
			  "width": jQuery(_div_wrap).width(),							
			  "height": jQuery(_div_wrap).height()};
		jQuery.fn.stickyNotes.notes.push(note);
        jQuery(_note_content).css({
            'width': jQuery("#note-" + note_id).width() - 44, 
            'height': jQuery("#note-" + note_id).height() - 15
        });
		
        jQuery.fn.stickyNotes.createScrollBarNote(note);
		if (jQuery.fn.stickyNotes.options.createCallback) {
			jQuery.fn.stickyNotes.options.createCallback(note);
		}		
	}
	
	//Update side bar note, called when clicked on any scroll bar note
	jQuery.fn.stickyNotes.updateSideBarNote = function(div_square_box_anchor,note_id) {
		var currentlyEditedNoteText = jQuery("#note-" + note_id).find("textarea").val();
		$(div_square_box_anchor).text(currentlyEditedNoteText);
	}
	
	jQuery.fn.stickyNotes.stopEditing = function(note_id) {
		var note = jQuery.fn.stickyNotes.getNote(note_id);
		note.text = $("#note-" + note_id).find('textarea').val();
		var _p_note_text = 	$(document.createElement('p')).attr("id", "p-note-" + note_id)
							.html(note.text);
		$("#note-" + note_id).find('textarea').replaceWith(_p_note_text); 
		$("#p-note-" + note_id).dblclick(function() {
			jQuery.fn.stickyNotes.editNote(this);
		});	
		jQuery.fn.stickyNotes.removeCurrentlyEditedNote(note_id);
		if (jQuery.fn.stickyNotes.options.editCallback) {
			jQuery.fn.stickyNotes.options.editCallback(note);
		}
	};
	
	//Delete note, called when delete button is clicked
	jQuery.fn.stickyNotes.deleteNote = function() {
		var note_id = jQuery("#sticky-container").children().attr("id").replace(/note-/, "");
		var note = jQuery.fn.stickyNotes.getNote(note_id);
		jQuery("#note-" + note_id).remove();
		jQuery("#s_note-" + note_id).remove();
		if (jQuery.fn.stickyNotes.options.deleteCallback) {
			jQuery.fn.stickyNotes.options.deleteCallback(note);
		}
		
		jQuery.fn.stickyNotes.deleteNoteFromArray(note_id);	
		
		var newIndex = jQuery.fn.stickyNotes.notes.length - 1;
		var noteToLoad = jQuery.fn.stickyNotes.notes[newIndex];
		jQuery.fn.stickyNotes.renderNote(noteToLoad);
	}
	
	//Note is made editable, called when an existing note is double clicked
	jQuery.fn.stickyNotes.editNote = function(paragraph) {
		var note_id = jQuery(paragraph).parent().parent().attr("id").replace(/note-/, "");
		var textarea = 	$(document.createElement('textarea')).attr("id", "textarea-note-" + note_id)
							.val(
									$("#note-" + note_id)
									.find('p')
									.html()
									);

		$("#p-note-" + note_id).replaceWith(textarea);

        jQuery(textarea).css({
            'width': jQuery("#note-" + note_id).width() - 44, 
            'height': jQuery("#note-" + note_id).height() - 15
        });
		jQuery("#note-" + note_id).find("textarea").focus();
		jQuery.fn.stickyNotes.setCurrentlyEditedNote(note_id);
	}
	
	jQuery.fn.stickyNotes.currentlyEditedNotes = function() {
		return jQuery.fn.stickyNotes.currentlyEditedNoteIds;
	}
	
	jQuery.fn.stickyNotes.setCurrentlyEditedNote = function(note_id) {
		jQuery.fn.stickyNotes.currentlyEditedNoteIds.push(note_id);
	}	
	
	jQuery.fn.stickyNotes.removeCurrentlyEditedNote = function(note_id) {
		var notes = jQuery.fn.stickyNotes.currentlyEditedNotes();
		var pos = -1;
		for (var i = notes.length - 1; i >= 0; i--){
			if (notes[i] == note_id) {
				pos = i;
				break;
			}
		};
		jQuery.fn.stickyNotes.currentlyEditedNoteIds.splice(pos, 1);
	}
	
	//Render existing notes i.e. notes from option.notes array
	jQuery.fn.stickyNotes.renderNote = function(note) {
		var _p_note_text = 	$(document.createElement('p')).attr("id", "p-note-" + note.id)
							.html( note.text);		
		var _div_note 	= 	jQuery(document.createElement('div')).addClass('jStickyNote');
        var _div_background = jQuery.fn.stickyNotes.createNoteBackground();
		_div_note.append(_p_note_text);
		var _div_wrap 	= 	$(document.createElement('div'))
							.css({'position':'absolute','top':note.pos_y,'left':note.pos_x, 'float': 'left',"width":500,"height":500})
							.attr("id", "note-" + note.id)
							.append(_div_background)
							.append(_div_note);
		_div_wrap.addClass('jSticky-medium');
		if (jQuery.fn.stickyNotes.options.resizable) {
			_div_wrap.resizable({stop: function(event, ui) { jQuery.fn.stickyNotes.resizedNote(note.id)}});
		}

		$('#sticky-container').append(_div_wrap);
		
		jQuery("#note-" + note.id).click(function() {
			return false;
		})
		$(_p_note_text).dblclick(function() {
			jQuery.fn.stickyNotes.editNote(this);
		});		
	}
	
	//Creates a note in the scroll bar, called while rendering and when a new note is created
	jQuery.fn.stickyNotes.createScrollBarNote = function(note) {
		var _div_square_box = jQuery(document.createElement('div')).addClass('square-box');
		_div_square_box.attr("id", "s_note-" + note.id);
		var _div_square_box_content = jQuery(document.createElement('div')).addClass('square-content');
		var _div_square_box_anchor = $('<a></a>').attr("href","#").addClass('fill-div');
		_div_square_box_anchor.click(function(){
			jQuery("#s_note-" + note.id).focus();
			jQuery.fn.stickyNotes.updateSideBarNote(this, note.id);
			jQuery.fn.stickyNotes.renderNote(jQuery.fn.stickyNotes.notes[note.id - 1]);
		});
		_div_square_box_anchor.text(note.text);
		_div_square_box_content.append(_div_square_box_anchor);
		_div_square_box.append(_div_square_box_content);
		
		$('#scroll-box').prepend(_div_square_box);
	}
	
	jQuery.fn.stickyNotes.movedNote = function(note_id) {
		var note = jQuery.fn.stickyNotes.getNote(note_id);
		note.pos_x=jQuery("#note-" + note_id).css("left").replace(/px/, "");
		note.pos_y=jQuery("#note-" + note_id).css("top").replace(/px/, "");
		if (jQuery.fn.stickyNotes.options.moveCallback) {
			jQuery.fn.stickyNotes.options.moveCallback(note);
		}		
	}
	
	jQuery.fn.stickyNotes.resizedNote = function(note_id) {
		var note = jQuery.fn.stickyNotes.getNote(note_id);
		note.width=jQuery("#note-" + note_id).width();
		note.height=jQuery("#note-" + note_id).height();
		if (jQuery.fn.stickyNotes.options.resizeCallback) {
			jQuery.fn.stickyNotes.options.resizeCallback(note);
		}		
	}
	
    jQuery.fn.stickyNotes.createNoteBackground = function() {
        var background = null;
        if (jQuery.browser.msie && jQuery.browser.version <= 6)  {
            background = $(document.createElement('div')).addClass("background").html('<img src="images/spacer.gif" class="stretch" style="margin-top:5px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'images/sticky-bg.png\',sizingMethod=\'scale\'");" alt="" />');
        } else {
            background = $(document.createElement('div')).addClass("background").html('<img src="images/sticky-bg.png" class="stretch" style="margin-top:5px;" alt="" />');
        }
        return background;
    }
	
	jQuery.fn.stickyNotes.defaults = {
		notes 	: [],
		resizable : false,
		controls: true,
		editCallback: false, 
		createCallback: false,
		deleteCallback: false,
		moveCallback: false,
		resizeCallback: false
	};
	
	jQuery.fn.stickyNotes.options = null;
	jQuery.fn.stickyNotes.currentlyEditedNoteIds = new Array();
	jQuery.fn.stickyNotes.notes = new Array();
})(jQuery);
