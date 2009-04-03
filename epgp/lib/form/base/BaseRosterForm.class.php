<?php

/**
 * Roster form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRosterForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'        => new sfWidgetFormInputHidden(),
      'name'      => new sfWidgetFormInput(),
      'charclass' => new sfWidgetFormPropelChoice(array('model' => 'Classes', 'add_empty' => false)),
      'ep'        => new sfWidgetFormInput(),
      'gp'        => new sfWidgetFormInput(),
      'priority'  => new sfWidgetFormInput(),
      'joined_on' => new sfWidgetFormDateTime(),
      'is_active' => new sfWidgetFormInputCheckbox(),
    ));

    $this->setValidators(array(
      'id'        => new sfValidatorPropelChoice(array('model' => 'Roster', 'column' => 'id', 'required' => false)),
      'name'      => new sfValidatorString(array('max_length' => 255)),
      'charclass' => new sfValidatorPropelChoice(array('model' => 'Classes', 'column' => 'id')),
      'ep'        => new sfValidatorNumber(),
      'gp'        => new sfValidatorNumber(),
      'priority'  => new sfValidatorNumber(),
      'joined_on' => new sfValidatorDateTime(),
      'is_active' => new sfValidatorBoolean(),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorPropelUnique(array('model' => 'Roster', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('roster[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Roster';
  }


}
