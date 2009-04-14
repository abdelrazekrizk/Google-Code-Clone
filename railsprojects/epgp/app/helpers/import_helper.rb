module ImportHelper
  def new_character_form(data) #data is a hash of :name :charclass :charrace
    name = data[:name]
    t = "<tr>"
    t += import_text_field("attendees[#{name}][name]", name)
    classes = Hash[]
    CharacterClass.find(:all, :order => "name ASC").each do |c|
      classes[c.id] = c.name
    end
    t += import_choice_box("attendees[#{name}][charclass]", classes, data[:charclass])
    
    races = Hash[]
    CharacterRace.find(:all, :order => "name ASC").each do |r|
      races[r.id] = r.name
    end
    t += import_choice_box("attendees[#{name}][charrace]", races, data[:charrace])
    t += import_check_box("attendees[#{name}][import]", true)
    t += "</tr>"
    return t
  end
  
  def import_text_field(id, default)
    "<td><input name=\"#{id}\" type=\"text\" value=\"#{default}\" /></td>"
  end
  
  def import_choice_box(name, choices, default)
    t = "<td><select name=\"#{name}\">"
    choices.each do |id, choice|
      if(choice == default)
        t += "<option value=\"#{id}\" selected=\"selected\">#{choice}</option>"
      else
        t += "<option value=\"#{id}\">#{choice}</option>"
      end
    end
    t += "</select></td>"
  end
  
  def import_check_box(id, default)
    if(default)
      "<td><input name=\"#{id}\" type=\"checkbox\" checked=\"checked\" value=\"1\"/></td>"
    else
      "<td><input name=\"#{id}\" type=\"checkbox\" /></td>"
    end
  end
  
  def import_hidden_field(id, data)
    "<input type=\"hidden\" name=\"#{id}\" value=\"#{data}\" />"
  end
  
  def item_row(item, num) # item is hash of :name, :wowitemid, :awardee
    name = item[:itemname]
    t = ""
    t += import_text_field("items[#{num}][name]", name)
    characters = Hash[]
    Character.find_all_by_is_active(true, :order => "name ASC").each do |c|
      characters[c.id] = c.name
    end
    t += import_choice_box("items[#{num}][awardee]", characters, item[:awardee])
    t += import_text_field("items[#{num}][value]", Item.get_value_for(item[:wowitemid]))
    t += import_text_field("items[#{num}][wowitemid]", item[:wowitemid])
    t += import_check_box("items[#{num}][import]", true)
    return t
  end
end
