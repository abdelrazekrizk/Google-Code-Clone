class CharacterController < ApplicationController
  def index
    @characters = Character.find(:all, :order => 'name asc')
  end
  
  def show
    @character = Character.find_by_name(params[:id]);
  end
end
