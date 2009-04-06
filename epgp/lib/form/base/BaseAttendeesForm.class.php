<?php

/**
 * Attendees form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseAttendeesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'raids_id'  => new sfWidgetFormInputHidden(),
      'roster_id' => new sfWidgetFormInputHidden(),
    ));

    $this->setValidators(array(
      'raids_id'  => new sfValidatorPropelChoice(array('model' => 'Raids', 'column' => 'id', 'required' => false)),
      'roster_id' => new sfValidatorPropelChoice(array('model' => 'Roster', 'column' => 'id', 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('attendees[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Attendees';
  }


}
