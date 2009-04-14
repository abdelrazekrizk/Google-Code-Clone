class CharactersController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  
  def index
    @characters = Character.find_all_by_is_active(true, :order => 'name asc')
  end
  
  def show
    @character = Character.find_by_name(params[:id]);
  end
  
  def adminindex
    @characters = Character.find(:all, :order => 'name asc')
  end
  
  def edit
    @character = Character.find(params[:id])
  end
  
  def update
    c = Character.find(params[:character][:id])
    if(c.update_attributes(params[:character]))
    #if(c.save)
      flash[:notice] = "Updated successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not update successfully."
      redirect_to :action => 'edit'
    end
  end
  
  def toggle_activation
    char = Character.find(params[:id])
    char.is_active ^= true
    if(char.save)
      flash[:notice] = char.name + "'s active status toggled successfully"
    else
      flash[:error] = char.name + "'s active status toggle unsuccessful"
    end
    redirect_to :action => 'adminindex'
  end
  
  def new
  end
  
  def create
    c = Character.new
    if(c.update_attributes(params[:character]))
      flash[:notice] = "Created successfully"
      redirect_to :action => 'adminindex'
    else
      flash[:notice] = "Did not create successfully."
      redirect_to :action => "edit"
    end
  end
  
end
